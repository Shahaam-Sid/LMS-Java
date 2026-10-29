import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import api from "../../api/axios";
import LiveError from "../LiveError";

export default function ReserveFormComponent() {
    const [ID, setID] = useState("");
    const [ISBN, setISBN] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        switch (e.target.name) {
            case "ID":
                setID(e.target.value);
                setValidationErrors(validators.ID(e.target.value));
                break;
            case "ISBN":
                setISBN(e.target.value);
                setValidationErrors(validationErrors.ISBN(e.target.value));
                break;
            default:
                break;
        }
        setIsInvalid(validationErrors.ID !==  "" && validationErrors.ISBN !== "")
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true)
        try {
            await reserve(
                ID,
                ISBN
            );
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not reserve");
            setLoading(false)
        }
    }

    return (
            <div className="m-0 flex justify-cente">
                <div className="m-10 w-xl bg-purple-400 border-none rounded-2xl p-3.5">
                   <form onSubmit={handleSubmit}>
                        <FormField label="Member ID" name="ID" value={ID} minlength={9} maxlenght={9} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.ID} />
                        <FormField label="ISBN" name="ISBN" value={ISBN} minlength={10} maxlenght={13} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.ISBN} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                        <div className="text-center font-asap-sharp">
                            <button type="submit" disabled={isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                                {loading ? "Loading..." : "Search"}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        );

}

async function reserve(ID, ISBN) {
    const res = await api.post('api/v1/reservations',
        ID,
        ISBN
    )
}