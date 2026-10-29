import { useState } from "react";
import renameBookForm from "../../forms/renameBookForm"
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";

export default function UpdateBookFormComponent() {
    const [form, setForm] = useState(renameBookForm);
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);

    function handleChange(e) {
        const {name, value} = e.target;
        setForm((prev) => ({...prev, [name]: value}));

        const validate = validators[name];
        if (validate) {
            const errorMsg = validate(value);
            setValidationErrors(prev => ({...prev, [name]: errorMsg}))
        }
        setIsInvalid(Object.values(validationErrors).every(value => !value));
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            await PhyscalBookUpdate(
                form.shelfLocation,
                form.totalCopies,
                form.availableCopies
            )
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not update book")
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-emerald-400 border-none rounded-2xl p-3.5">
               <form onSubmit={handleSubmit}>
                    <FormField label="ISBN" name="ISBN" value={form.ISBN} minlength={10} maxlenght={13} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.ISBN} />
                    <FormField label="Shelf Location" name="shelfLocation" value={form.shelfLocation}  onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.shelfLocation} />
                    <FormField label="Total Copies" type="number" name="totalCopies" value={form.totalCopies} min={1} onChange={handleChange} />
                    <FormField label="Available Copies" type="number" name="availableCopies" value={form.availableCopies} min={1} onChange={handleChange} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Update"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

async function PhyscalBookUpdate(ISBN, shelfLocation, totalCopies, availableCopies) {
    const res = await api.patch(`api/v1/books/${ISBN}`,
        {
            shelfLocation,
            totalCopies,
            availableCopies
        }
    );
}