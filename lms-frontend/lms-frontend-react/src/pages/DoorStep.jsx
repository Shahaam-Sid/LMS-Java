import LargeButtons from "../components/LargeButtons";

export default function DoorStep() {
    
    return(
        <div className="fixed inset-0 z-10 flex flex-col text-2xl min-h-dvh justify-center items-center text-center">
            <h2 className="font-montserrat">Welcome</h2> <br />
            <LargeButtons color="green" link={"/login"}>Login</LargeButtons>
            <br />
            <LargeButtons color="blue" link={"/signup"}>Signup</LargeButtons>

        </div>
    );
}