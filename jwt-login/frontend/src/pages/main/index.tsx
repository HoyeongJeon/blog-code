import { useAuthStore } from "../../stores/auth";
import { useEffect } from "react";
import { useApi } from "../../api/useApi";

export default function Main() {
  const logout = useAuthStore((state) => state.logout);
  const api = useApi();
  const handleLogout = async () => {
    await api.post("/v1/logout");
    logout();
  };

  useEffect(() => {
    const healthCheck = async () => {
      try {
        const response = await api.get("/health");
        console.log(response.data);
      } catch (error) {
        console.error("헬스체크에 실패했습니다.", error);
      }
    };

    healthCheck();
  }, []);

  return (
    <>
      <h1>로그인 되었습니다.</h1>
      <button onClick={handleLogout}>로그아웃</button>
    </>
  );
}
