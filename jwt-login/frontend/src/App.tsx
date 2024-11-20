import { Routes, Route } from "react-router-dom";
import "./App.css";
import Login from "./pages/login";
import Signup from "./pages/signup";
import ProtectedRoute from "./components/routes/ProtectedRoute";
import Main from "./pages/main";
import AnnonymousRoute from "./components/routes/AnonymousRoute";

function App() {
  return (
    <>
      <Routes>
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Main />
            </ProtectedRoute>
          }
        />
        <Route
          path="/login"
          element={
            <AnnonymousRoute>
              <Login />
            </AnnonymousRoute>
          }
        />
        <Route
          path="/signup"
          element={
            <AnnonymousRoute>
              <Signup />
            </AnnonymousRoute>
          }
        />
      </Routes>
    </>
  );
}

export default App;
