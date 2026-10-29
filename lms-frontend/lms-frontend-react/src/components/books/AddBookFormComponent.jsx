import { useState } from "react";
import bookForm from "../../forms/bookForm"
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";

export default function AddBookFormComponent() {
    const [form, setForm] = useState(bookForm);
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);
    const [isFilled, setIsFilled] = useState(false);

    function handleChange(e) {
        const {name, value} = e.target;
        setForm((prev) => ({...prev, [name]: value}));

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
            await PhyscalBookCreate(
                form.ISBN,
                form.title,
                form.author,
                form.genre,
                form.publishedYear,
                form.shelfLocation,
                form.totalCopies
            )
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not add book")
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-cente">
            <div className="m-10 w-xl bg-emerald-400 border-none rounded-2xl p-3.5">
               <form onSubmit={handleSubmit}>
                    <FormField label="ISBN" name="ISBN" value={form.ISBN} minlength={10} maxlenght={13} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.ISBN} />
                    <FormField label="Title" name="title" value={form.title} maxlenght={50} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.title} />
                    <FormField label="Author" name="author" value={form.author} maxlenght={30} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.author} />
                    <FormField label="Genre" name="genre" value={form.genre} maxlenght={20} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.genre} />
                    <FormField label="Published Year" type="number" name="publishedYear" value={form.publishedYear} min={-1000} max={`${new Date().getFullYear()}`} onChange={handleChange} />
                    <FormField label="Shelf Location" name="shelfLocation" value={form.shelfLocation}  onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.shelfLocation} />
                    <FormField label="Total Copies" type="number" name="totalCopies" value={form.totalCopies} min={1} onChange={handleChange} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={!isFilled || isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Add"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

async function PhyscalBookCreate(ISBN, title, author, genre, publishedYear, shelfLocation, totalCopies) {
    const res = await api.post("api/v1/books",
        {
            ISBN,
            title,
            author,
            genre,
            publishedYear,
            shelfLocation,
            totalCopies,
        }
    );
}