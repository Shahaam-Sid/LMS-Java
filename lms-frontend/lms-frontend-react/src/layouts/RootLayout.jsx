import AuthInterceptorSetup from "../components/AuthInterceptorSetup";
import TopBar from "../components/TopBar";
import { Outlet } from "react-router-dom";

export default function RootLayout() {
    return(
        <>
            <AuthInterceptorSetup />
            <TopBar />
            <Outlet />
        </>
    );
}