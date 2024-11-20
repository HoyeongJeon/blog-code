import { useState } from "react";
import { Link } from "react-router-dom";
import { useAuthStore } from "../../stores/auth";
import { useNavigate } from "react-router-dom";
import { useApi } from "../../api/useApi";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const login = useAuthStore((state) => state.login);
  const navigate = useNavigate();
  const api = useApi();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    let req = {
      email,
      password,
    };

    try {
      await api.post("/auth/login", req);
      login();
      navigate("/");
    } catch (error) {
      console.error("로그인 실패", error);
    }
  };

  return (
    <>
      <main>
        <h1>로그인 안됨</h1>
        <section>
          <form onSubmit={handleSubmit}>
            <div>
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(event) => {
                  setEmail(event.target.value);
                }}
              />
            </div>
            <div>
              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(event) => {
                  setPassword(event.target.value);
                }}
              />
            </div>

            <button type="submit">Login</button>
            <div>
              <Link to="/signup">회원가입</Link>
            </div>
          </form>
        </section>
      </main>
    </>
  );
}
