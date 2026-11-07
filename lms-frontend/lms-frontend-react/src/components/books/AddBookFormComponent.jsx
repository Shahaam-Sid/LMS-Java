import { useState } from "react";
import bookForm from "../../forms/bookForm"
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import BookTypeDropdown from "../BookTypeDropDown";
import api from "../../api/axios";

const physicalBookFields = [
    "ISBN", "title", "author", "genre",
    "publishedYear", "shelfLocation", "totalCopies",
    ];
const eBookFields = [
    "ISBN", "title", "author", "genre",
    "publishedYear", "downloadURL", "format", "fileSizeMB"
    ];
const audioBookField = [
    "ISBN", "title", "author", "genre",
    "publishedYear", "downloadURL", "format", "fileSizeMB", "narrator"
]

export default function AddBookFormComponent() {
    const [form, setForm] = useState(bookForm);
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [type, setType] = useState("Book");

    const isFilled = checkIsFilled(type);
    const hasErrors = Object.values(validationErrors).some(msg => msg);

    function handleChange(e) {
        const {name, value} = e.target;
        setForm((prev) => ({...prev, [name]: value}));

        const validate = validators[name];
        if (validate) {
            const errorMsg = validate(value);
            setValidationErrors(prev => ({...prev, [name]: errorMsg}))
        }
    }

    function handleBookTypeChange(selectedOption) {
        setType(selectedOption);
        setForm(bookForm)
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            switch (type) {
                case "Book":
                    await PhyscalBookCreate(
                        form.ISBN,
                        form.title,
                        form.author,
                        form.genre,
                        form.publishedYear,
                        form.shelfLocation,
                        form.totalCopies
                    )
                    break;
                case "EBook":
                    await EBookCreate(
                        form.ISBN,
                        form.title,
                        form.author,
                        form.genre,
                        form.publishedYear,
                        form.downloadURL,
                        form.format,
                        form.fileSizeMB,
                    )
                    break;
                case "Audio Book":
                    await AudioBookCreate(
                        form.ISBN,
                        form.title,
                        form.author,
                        form.genre,
                        form.publishedYear,
                        form.downloadURL,
                        form.format,
                        form.fileSizeMB,
                        form.narrator
                    )
                    break;
                default:
                    break;
            }
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not add book")
        } finally {
            setLoading(false);
            setForm(bookForm)
        }
    }

    function checkIsFilled(type) {
        switch (type) {
            case "Book":
                return physicalBookFields.every(
                    (key) => String(form[key] ?? "").trim() !== ""
                  );
            case "EBook":
                return eBookFields.every(
                    (key) => String(form[key] ?? "").trim() !== ""
                );
            case "Audio Book":
                return audioBookField.every(
                    (key) => String(form[key] ?? "").trim() !== ""
                )
            default:
                return false;
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-emerald-400 border-none rounded-2xl p-3.5">
                <BookTypeDropdown onChange={handleBookTypeChange}  />
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
                    { type == "Book" &&
                    <>
                        <FormField label="Shelf Location" name="shelfLocation" value={form.shelfLocation}  onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.shelfLocation} />
                        <FormField label="Total Copies" type="number" name="totalCopies" value={form.totalCopies} min={1} onChange={handleChange} />
                    </>
                    }
                    { type != "Book" &&
                    <>
                        <FormField label="Download URL" name="downloadURL" value={form.downloadURL} minlength={6} maxlenght={2000} onChange={handleChange} />
                        <FormField label="Format" name="format" value={form.format} minlength={1} maxlenght={30} onChange={handleChange} />
                        <FormField label="Size(mb)" name="fileSizeMB" value={form.fileSizeMB} type="number" step={0.01} min={0.01} onChange={handleChange} />
                    </>
                    }
                    { type == "Audio Book" &&
                        <>
                        <FormField label="Narrator" name="narrator" value={form.narrator} minlength={1} maxlenght={100} onChange={handleChange} />
                        </>
                    }
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
            type: "PHYSICAL"
        }
    );
    return res;
}
async function EBookCreate(ISBN, title, author, genre, publishedYear, downloadURL, format, fileSizeMB) {
    const res = await api.post("api/v1/books",
        {
            ISBN,
            title,
            author,
            genre,
            publishedYear,
            downloadURL,
            format,
            fileSizeMB,
            type: "EBOOK"
        }
    );
    return res;
}
async function AudioBookCreate(ISBN, title, author, genre, publishedYear, downloadURL, format, fileSizeMB, narrator) {
    const res = await api.post("api/v1/books",
        {
            ISBN,
            title,
            author,
            genre,
            publishedYear,
            downloadURL,
            format,
            fileSizeMB,
            narrator,
            type: "AUDIOBOOK"
        }
    );
    return res;
}