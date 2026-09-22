import SectionHeader from "../components/SectionHeader";
import { IconButton } from "../components/IconButtons";
import { PlusIcon, MinusIcon, GetIcon, SearchAllIcon, SearchAvailableIcon, SearchOneIcon, UpdateIcon } from "../components/Icons";

export default function Dashboard() {

    function onClick() {

    }
    

    return (
        <>
            <SectionHeader text="Books" color="text-emerald-600" colorRule="border-emerald-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Search"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <SearchOneIcon />
                </IconButton>
                <IconButton label={"Add"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Remove"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <MinusIcon />
                </IconButton>
                <IconButton label={"Get All"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <SearchAllIcon />
                </IconButton>
                <IconButton label={"Get Available"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
                <IconButton label={"Update"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <UpdateIcon />
                </IconButton>
            </div>
            <SectionHeader text="Members" color="text-sky-600" colorRule="border-sky-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Search"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <SearchOneIcon />
                </IconButton>
                <IconButton label={"Add"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Remove"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <MinusIcon />
                </IconButton>
                <IconButton label={"Get All"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <SearchAllIcon />
                </IconButton>
                <IconButton label={"Get Active"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
                <IconButton label={"Update"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <UpdateIcon />
                </IconButton>
            </div>
            <SectionHeader text="Transactions" color="text-rose-600" colorRule="border-rose-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Get Overdue"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <SearchOneIcon />
                </IconButton>
                <IconButton label={"Borrow"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Return"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <MinusIcon />
                </IconButton>
                <IconButton label={"Get All"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <SearchAllIcon />
                </IconButton>
                <IconButton label={"Get Active"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
                <IconButton label={"Update"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <UpdateIcon />
                </IconButton>
            </div>
            <SectionHeader text="Reservations" color="text-purple-600" colorRule="border-purple-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get Queue"} color={"bg-purple-400 hover:bg-purple-600"} stroke="stroke-purple-900" onClick={onClick}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Reserve"} color={"bg-purple-400 hover:bg-purple-600"} stroke="stroke-purple-900" onClick={onClick}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Get by Member"} color={"bg-purple-400 hover:bg-purple-600"} stroke="stroke-purple-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
            </div>
        </>
    );
}