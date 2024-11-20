import React from "react";
import { Navigate } from "react-router-dom";
import { useAuthStore } from "../../stores/auth";

interface AnonymousRouteProps {
  children: React.ReactNode;
}

const AnonymousRoute: React.FC<AnonymousRouteProps> = ({ children }) => {
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn);

  if (isLoggedIn) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
};

export default AnonymousRoute;
