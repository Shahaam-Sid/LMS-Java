import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useUser } from "../hooks/useUser";
import FormField from "../components/FormField";
import { validators } from "../utils/validations";
import LiveError from "../components/LiveError";

export default function LoginPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { logIn } = useUser();
    const [emailError, setEmailError] = useState("");
    const navigate = useNavigate();

    function handleChange(e) {
        switch (e.target.name) {
            case "email":
                setEmail(e.target.value);
                setEmailError(validators.email(e.target.value));
                break;
            case "password":
                setPassword(e.target.value);
                break;
            default:
                break;
        }
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            await logIn(
                email,
                password
            );
            navigate("/dashboard");
        } catch (err) {
            setError(err.response?.data?.message ?? "Could Not Login");
        } finally {
            setLoading(false)
        }
    }


    return(
        <div className="m-0 flex justify-center">
            <div className="m-10 mt-30 w-xl bg-green-200 border-none rounded-2xl p-3.5">
                <h1 className="text-center font-montserrat">Login</h1>
                <form onSubmit={handleSubmit}>
                    <FormField label= "Email" type="email" name = "email" value={email} onChange={handleChange} />
                    <LiveError errorMsg={emailError}/>
                    <FormField label= "Password" type="password" name = "password" value={password} onChange={handleChange} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={!email || !password || emailError || loading} className="cursor-pointer bg-blue-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Logging In" : "Log in"}
                        </button>
                    </div>
                    <p className="text-center font-asap-sharp">
                        Don't have an account? <Link to="/signup" className="hover:text-green-500" >Sign up</Link>
                    </p>
                </form>
            </div>
        </div>
    );
}