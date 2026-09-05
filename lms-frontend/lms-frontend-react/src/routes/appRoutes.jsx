import { createBrowserRouter } from "react-router-dom";
import { Outlet } from "react-router-dom";
import TopBar from "../components/TopBar";
import DoorStep from "../pages/DoorStep";
import SignupPage from "../pages/SignupPage";
import LoginPage from "../pages/LoginPage";

function RootLayout() {
    return(
        <>
            <TopBar />
            <Outlet />
        </>
    );
}

export const router = createBrowserRouter([
    {
        path: "/",
        element: <RootLayout />,
        children: [
            {path: "/door-step", element: <DoorStep />},
            {path: "/signup", element: <SignupPage />},
            {path: "/login", element: <LoginPage />},
        ]
    }
])