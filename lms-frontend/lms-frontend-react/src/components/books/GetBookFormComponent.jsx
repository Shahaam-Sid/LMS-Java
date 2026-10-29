import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";

export default function GetBookFormComponent() {
    const [ISBN, setISBN] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);

    function handleChange(e) {
        setISBN(e.target.value);

        const validate = validators[ISBN];
        if (validate) {
            const errorMsg = validate(e.target.value);
            setValidationErrors(prev => ({...prev, [ISBN]: errorMsg}))
        }
        setIsInvalid(Object.values(validationErrors).every(value => !value));
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            await GetBook(ISBN)
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not getS book")
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-emerald-400 border-none rounded-2xl p-3.5">
               <form onSubmit={handleSubmit}>
                    <FormField label="ISBN" name="ISBN" value={ISBN} minlength={10} maxlenght={13} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.ISBN} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Get"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

async function GetBook(ISBN) {
    const res = await api.get(`api/v1/books/${ISBN}`);
}