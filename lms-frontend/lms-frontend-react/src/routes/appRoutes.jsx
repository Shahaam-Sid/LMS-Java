import { createBrowserRouter } from "react-router-dom";
import RootLayout from "../layouts/RootLayout";
import RootRedirect from "../components/RootRedirect";
import DoorStep from "../pages/DoorStep";
import SignupPage from "../pages/SignupPage";
import LoginPage from "../pages/LoginPage";
import Dashboard from "../pages/Dashboard";
import GuestOnlyRoute from "../components/GuestOnlyRoutes";
import ProtectedRoute from "../components/ProtectedRoutes";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <RootLayout />,
        children: [
            { index: true, element: <RootRedirect /> },
            { path: "door-step", element: <DoorStep /> },
            { path: "signup", element: <GuestOnlyRoute><SignupPage /></GuestOnlyRoute> },
            { path: "login", element: <GuestOnlyRoute><LoginPage /></GuestOnlyRoute> },
            { path: "dashboard", element: <ProtectedRoute><Dashboard /></ProtectedRoute> }
        ]
    }
])