import { useState } from "react";
import FormField from "../FormField";
import api from "../../api/axios";

export default function SearchBookFormComponent() {
    const [query, setQuery] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        setQuery(e.target.value);
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            await SearchBook(query)
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not search book")
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-sky-600 border-none rounded-2xl p-3.5">
               <form onSubmit={handleSubmit}>
                    <FormField label="Search" name="search" value={query} onChange={handleChange} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={query === "" || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Search"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

async function SearchBook(query) {
    const res = await api.get(`api/v1/books/search?q=${query}`);
}