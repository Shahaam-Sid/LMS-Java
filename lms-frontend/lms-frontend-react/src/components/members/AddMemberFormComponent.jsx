    import { useState } from "react";
    import memberForm from "../../forms/memberForm"
    import { validators } from "../../utils/validations";
    import FormField from "../FormField";
    import LiveError from "../LiveError";
    import api from "../../api/axios";

    export default function AddMemberFormComponent() {
        const [form, setForm] = useState(memberForm);
        const [validationErrors, setValidationErrors] = useState({});
        const [error, setError] = useState("");
        const [loading, setLoading] = useState(false);

        const hasErrors = Object.values(validationErrors).some(msg => msg);
        const isFilled = Object.values(form).every(value => value);

        function handleChange(e) {
            const {name, value} = e.target;
            setForm((prev) => ({...prev, [name]: value}));

            const validate = validators[name];
            if (validate) {
                const errorMsg = validate(value);
                setValidationErrors(prev => ({...prev, [name]: errorMsg}))
            }
        }

        async function handleSubmit(e) {
            e.preventDefault();
            setError("");
            setLoading(true);
            try {
                await MemberCreate(
                    form.name,
                    form.phone,
                    form.email,
                    form.address,
                    form.birthYear
                )
            } catch (err) {
                setError(err.response?.data?.message ?? "member")
            } finally {
                setLoading(false);
                setForm(memberForm);
            }
        }

        return (
            <div className="m-0 flex justify-center">
                <div className="m-10 w-xl bg-sky-400 border-none rounded-2xl p-3.5">
                <form onSubmit={handleSubmit}>
                        <FormField label="Name" name="name" value={form.name} minlength={3} maxlenght={135} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.name} />
                        <FormField label="Phone" name="phone" type="tel" value={form.phone} maxlenght={11} minlength={11} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.phone} />
                        <FormField label="Email" name="email" value={form.email} type="email" maxlenght={255} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.email} />
                        <FormField label="Address" name="address" value={form.address} maxlenght={55} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.address} />
                        <FormField label="Birth Year" type="number" name="birthYear" value={form.birthYear} min={1920} max={`${new Date().getFullYear()}`} onChange={handleChange} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                        <div className="text-center font-asap-sharp">
                            <button type="submit" disabled={!isFilled || hasErrors || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                                {loading ? "Loading..." : "Add"}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }

    async function MemberCreate(name, phone, email, address, birthYear) {
        const res = await api.post("api/v1/members",
        { name,
            phone,
            email,
            address,
            birthYear}
        )
        return res.data;
    }