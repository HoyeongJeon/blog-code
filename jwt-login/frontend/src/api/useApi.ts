import { useAuthStore } from "../stores/auth";
import { createApi } from "./api";

export const useApi = () => {
  const logout = useAuthStore((state) => state.logout);
  return createApi(logout);
};
