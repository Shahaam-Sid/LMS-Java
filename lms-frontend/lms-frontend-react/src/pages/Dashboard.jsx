import SectionHeader from "../components/SectionHeader";
import { IconButton } from "../components/IconButtons";
import { PlusIcon, MinusIcon, CheckIcon } from "../components/Icons";

export default function Dashboard() {

    function onClick() {

    }
    

    return (
        <>
            <SectionHeader text="Books" color="text-emerald-600" colorRule="border-emerald-900" />
            <IconButton label={"Add"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                <CheckIcon />
            </IconButton>
            <SectionHeader text="Members" color="text-sky-600" colorRule="border-sky-900" />

            <SectionHeader text="Transactions" color="text-rose-600" colorRule="border-rose-900" />

            <SectionHeader text="Reservations" color="text-purple-600" colorRule="border-purple-900" />

        </>
    );
}