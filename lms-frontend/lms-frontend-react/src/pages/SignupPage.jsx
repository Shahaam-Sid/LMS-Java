import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useUser } from "../hooks/useUser";
import FormField from "../components/FormField";
import { validators } from "../utils/validations";
import LiveError from "../components/LiveError";

const initialForm = {
    name: "",
    phone: "",
    email: "",
    address: "",
    birthYear: "",
    password: ""
};

export default function SignupPage() {

    const [form, setForm] = useState(initialForm);
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);
    const [isFilled, setIsFilled] = useState(false);

    const { signIn } = useUser();
    const navigate = useNavigate();


    function handleChange(e) {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value}));

        const validate = validators[name];
        if (validate) {
            const errorMsg = validate(value);
            setValidationErrors(prev => ({...prev, [name]: errorMsg}))
        }

        setIsInvalid(Object.values(validationErrors).every(value => !value));
        setIsFilled(Object.values(form).every(value => value));
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            await signIn(
                form.name,
                form.phone,
                form.email,
                form.address,
                form.birthYear,
                form.password
            );
            navigate("/dashboard");
        } catch (err) {
            setError(err.respone?.data?.message ?? "Could not create account");
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-blue-200 border-none rounded-2xl p-3.5">
                <h1 className="text-center font-montserrat">Create Staff Account</h1>
                    <form onSubmit={handleSubmit}>
                        <FormField label= "Full Name" name = "name" value={form.name} minlength={3} maxlenght={35} onChange={handleChange}  />
                        <LiveError errorMsg={validationErrors.name}/>
                        <FormField label= "Phone" name = "phone" value={form.phone} minlength={11} maxlenght={11} onChange={handleChange}  />
                        <LiveError errorMsg={validationErrors.phone} />
                        <FormField label= "Email" type="email" name = "email" value={form.email} onChange={handleChange}  />
                        <LiveError errorMsg={validationErrors.email} />
                        <FormField label= "Address" name = "address" value={form.address} minlength={5} maxlenght={55} onChange={handleChange}  />
                        <FormField label= "Birth Year" type="number" name = "birthYear" value={form.birthYear} onChange={handleChange}  />
                        <FormField label= "Password" type="password" name = "password" minlength={8} value={form.password} onChange={handleChange}  />
                        <LiveError errorMsg={validationErrors.password} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                        <div className="text-center font-asap-sharp">
                            <button type="submit" disabled={!isFilled || isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                                {loading ? "Creating..." : "Sign Up"}
                            </button>
                        </div>
                    </form>
                <p className="text-center font-asap-sharp">
                    Already have an account? <Link to="/login" className="hover:text-blue-500" >Login</Link>
                </p>
            </div>
        </div>
        
    );
    
}