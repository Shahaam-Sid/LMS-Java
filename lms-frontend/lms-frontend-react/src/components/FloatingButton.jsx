
export default function FloatingButton({text = "", onClick}) {

    return (
        <button onClick={onClick} type="button" className="fixed bottom-6 right-6 z-50 flex text-center justify-center rounded-full bg-red-600 px-5 py-3 text-sm text-white shadow-lg transition-all duration-200 hover:bg-red-700 hover:shadow-xl active:scale-95 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2">
            {text}
        </button>
    );
}