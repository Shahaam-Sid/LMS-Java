export default function LiveError({ errorMsg = "" }) {
    return (
        <div className="ml-4 mb-4">
            {errorMsg && <p className="text-red-700 whitespace-pre-line font-ibm-plex text-sm">{errorMsg}</p>}
        </div>
    );
}