import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useApi } from "../../api/useApi";

export default function Signup() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const api = useApi();
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    let req = {
      email,
      password,
    };

    try {
      const response = await api.post("/auth/signup", req);

      if (response.data) {
        alert("회원가입 성공");
        navigate("/login");
      } else {
        alert("회원가입 실패");
      }
    } catch (error) {
      console.error("회원가입 오류:", error);
      alert("회원가입 중 오류가 발생했습니다.");
    }
  };

  return (
    <>
      <main>
        <h1>회원가입</h1>
        <section>
          <form onSubmit={handleSubmit}>
            <div>
              <input
                type="email"
                placeholder="이메일"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
              />
            </div>
            <div>
              <input
                type="password"
                placeholder="비밀번호"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              />
            </div>
            <button type="submit">회원가입</button>
          </form>
        </section>
      </main>
    </>
  );
}
