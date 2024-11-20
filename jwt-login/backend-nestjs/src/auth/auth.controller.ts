import {
  Body,
  Controller,
  Get,
  Post,
  Res,
  UseGuards,
  Req,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { LoginDto, SignupDto } from './dto/auth.dto';
import { Response } from 'express';
import { AuthGuard } from '@nestjs/passport';
import { Request } from 'express';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('login')
  async login(
    @Res({ passthrough: true }) response: Response,
    @Body() loginDto: LoginDto,
  ) {
    const accessToken = await this.authService.login(loginDto);
    response.cookie('access_token', accessToken, {
      httpOnly: true,
    });
    return true;
  }

  @Post('logout')
  async logout(
    @Req() request: Request,
    @Res({ passthrough: true }) response: Response,
  ) {
    const accessToken = request.cookies?.['access_token'];
    this.authService.logout(accessToken);
    response.clearCookie('access_token');
    return true;
  }

  @Post('signup')
  async signup(@Body() signupDto: SignupDto) {
    return this.authService.signup(signupDto);
  }

  @Post('rotate')
  async rotate(
    @Req() request: Request,
    @Res({ passthrough: true }) response: Response,
  ) {
    const accessToken = request.cookies?.['access_token'];
    if (!accessToken) {
      return response.status(401).json({ message: '토큰이 없습니다.' });
    }
    const newAccessToken = await this.authService.rotate(accessToken);
    response.cookie('access_token', newAccessToken, {
      httpOnly: true,
    });
    return '토큰 갱신 완료';
  }

  @Get('health')
  @UseGuards(AuthGuard('jwt'))
  async health() {
    return '건강합니다. - NestJS';
  }
}
