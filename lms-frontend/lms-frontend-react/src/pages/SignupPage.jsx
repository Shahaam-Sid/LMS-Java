import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useUser } from "../hooks/useUser";
import FormField from "../components/FormField";

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
    
}