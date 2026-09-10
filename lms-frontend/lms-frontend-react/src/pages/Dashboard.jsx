import { useUser } from "../hooks/useUser";
import FloatingButton from "../components/FloatingButton";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {

    const { logOut } = useUser();
    const navigate = useNavigate();

    function onClick() {
        logOut();
        navigate("/door-step", {replace: true});
    }

    return (
        <>
            <FloatingButton text="Logout" onClick={onClick}/>
        </>
    );
}