import { Injectable, Inject, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './domain/entity/user.entity';
import { LoginDto, SignupDto } from './dto/auth.dto';
import { Redis } from 'ioredis';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User)
    private userRepository: Repository<User>,
    private jwtService: JwtService,
    @Inject('REDIS_CLIENT') private readonly redisClient: Redis,
  ) {}

  async login(loginDto: LoginDto) {
    const user = await this.userRepository.findOne({
      where: {
        email: loginDto.email,
      },
    });

    const { accessToken, refreshToken } = await this.generateToken(
      user.email,
      user.id,
    );

    await this.redisClient.set(
      `refresh_token:${user.id}`,
      refreshToken,
      'EX',
      7 * 24 * 60 * 60, // 7일
    );

    return accessToken;
  }

  async signup(signupDto: SignupDto) {
    const user = this.userRepository.create(signupDto);
    await this.userRepository.save(user);
    return true;
  }

  async logout(accessToken: string) {
    const { sub: userId } = this.jwtService.decode(accessToken) as {
      sub: number;
    };
    await this.redisClient.del(`refresh_token:${userId}`);
    return true;
  }

  async rotate(expiredToken: string) {
    try {
      const decoded = this.jwtService.decode(expiredToken) as {
        sub: number;
      };
      if (!decoded || !decoded.sub) {
        throw new UnauthorizedException('토큰이 올바르지 않습니다.');
      }

      const storedRefreshToken = await this.redisClient.get(
        `refresh_token:${decoded.sub}`,
      );

      if (!storedRefreshToken) {
        throw new UnauthorizedException('토큰이 만료되었습니다.');
      }

      try {
        this.jwtService.verify(storedRefreshToken);
      } catch (error) {
        await this.redisClient.del(`refresh_token:${decoded.sub}`);
        throw new UnauthorizedException('유효하지 않은 리프레시 토큰입니다.');
      }

      const user = await this.userRepository.findOne({
        where: {
          id: decoded.sub,
        },
      });

      const newAccessToken = await this.generateAccessToken(
        user.email,
        user.id,
      );

      return newAccessToken;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  private async generateToken(email: string, id: number) {
    return {
      accessToken: await this.generateAccessToken(email, id),
      refreshToken: await this.generateRefreshToken(email, id),
    };
  }

  private async generateAccessToken(email: string, id: number) {
    const payload = { email, sub: id, type: 'access' };
    return this.jwtService.sign(payload, {
      secret: 'secret',
      expiresIn: '5s',
    });
  }

  private async generateRefreshToken(email: string, id: number) {
    const payload = { email, sub: id, type: 'refresh' };
    return this.jwtService.sign(payload, {
      secret: 'secret',
      expiresIn: '7d',
    });
  }
}
