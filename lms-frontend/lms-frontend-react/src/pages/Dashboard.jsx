import { useState } from "react";
import SectionHeader from "../components/SectionHeader";
import { IconButton } from "../components/IconButtons";
import { PlusIcon, MinusIcon, GetIcon, SearchAllIcon, SearchAvailableIcon, SearchOneIcon, UpdateIcon } from "../components/Icons";
import OverlayForm from "../components/Overlay";
import AddBookFormComponent from "../components/books/AddBookFormComponent";
import GetBookFormComponent from "../components/books/GetBookFormComponent";
import RemoveBookFormComponent from "../components/books/RemoveBookFormComponent";
import UpdateBookFormComponent from "../components/books/UpdateBookFormComponent";
import SearchBookFormComponent from "../components/books/SearchBookFormComponent";
import GetMemberFormComponent from "../components/members/GetMemberFormComponent";
import SearchMemberFormComponent from "../components/members/SearchMemberFormComponent"
import AddMemberFormComponent from "../components/members/AddMemberFormComponent"
import GetTransactionFormComponent from "../components/transactions/GetTransactionFormComponent";
import BorrowFormComponent from "../components/transactions/BorrowFormComponent";
import ReturnFormComponent from "../components/transactions/ReturnFormComponent";
import ReserveFormComponent from "../components/reservations/ReserveFormComponent";
import GetQueueFormComponent from "../components/reservations/GetQueueFormComponent";
import GetMemberResevationsFormComponent from "../components/reservations/GetMemberReservationsFormComponent";

export default function Dashboard() {

    const [activeForm, setActiveForm] = useState(null);
    const close = () => setActiveForm(null);

    function onClick() {

    }
    

    return (
        <>
            <SectionHeader text="Books" color="text-emerald-600" colorRule="border-emerald-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={() => setActiveForm("Get Book")}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Search"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={() => setActiveForm("Search Book")}>
                    <SearchOneIcon />
                </IconButton>
                <IconButton label={"Add"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={() => setActiveForm("Add Book")}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Remove"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={() => setActiveForm("Remove Book")}>
                    <MinusIcon />
                </IconButton>
                <IconButton label={"Get All"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <SearchAllIcon />
                </IconButton>
                <IconButton label={"Get Available"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
                <IconButton label={"Update"} color={"bg-emerald-400 hover:bg-emerald-600"} stroke="stroke-emerald-900" onClick={() => setActiveForm("Update Book")}>
                    <UpdateIcon />
                </IconButton>
            </div>
            <SectionHeader text="Members" color="text-sky-600" colorRule="border-sky-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-emerald-900" onClick={() => setActiveForm("Get Member")}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Search"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={() => setActiveForm("Search Member")}>
                    <SearchOneIcon />
                </IconButton>
                <IconButton label={"Add"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={() => setActiveForm("Add Member")}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Get All"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <SearchAllIcon />
                </IconButton>
                <IconButton label={"Get Active"} color={"bg-sky-400 hover:bg-sky-600"} stroke="stroke-sky-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
            </div>
            <SectionHeader text="Transactions" color="text-rose-600" colorRule="border-rose-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={() => setActiveForm("Get Transaction")}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Get Overdue"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <SearchOneIcon />
                </IconButton>
                <IconButton label={"Borrow"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={() => setActiveForm("Borrow")}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Return"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={() => setActiveForm("Return")}>
                    <MinusIcon />
                </IconButton>
                <IconButton label={"Get All"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <SearchAllIcon />
                </IconButton>
                <IconButton label={"Get Active"} color={"bg-rose-400 hover:bg-rose-600"} stroke="stroke-rose-900" onClick={onClick}>
                    <SearchAvailableIcon />
                </IconButton>
            </div>
            <SectionHeader text="Reservations" color="text-purple-600" colorRule="border-purple-900" />
            <div className="m-5 mx-8 flex flex-row flex-wrap justify-between" >
                <IconButton label={"Get Queue"} color={"bg-purple-400 hover:bg-purple-600"} stroke="stroke-purple-900" onClick={() => setActiveForm("Get Queue")}>
                    <GetIcon />
                </IconButton>
                <IconButton label={"Reserve"} color={"bg-purple-400 hover:bg-purple-600"} stroke="stroke-purple-900" onClick={() => setActiveForm("Reserve")}>
                    <PlusIcon />
                </IconButton>
                <IconButton label={"Get by Member"} color={"bg-purple-400 hover:bg-purple-600"} stroke="stroke-purple-900" onClick={() => setActiveForm("Get Member List")}>
                    <SearchAvailableIcon />
                </IconButton>
            </div>


            {activeForm && (
                <OverlayForm onClose={close}>
                    {activeForm === "Get Book" && <GetBookFormComponent />}
                    {activeForm === "Add Book" && <AddBookFormComponent />}
                    {activeForm === "Remove Book" && <RemoveBookFormComponent />}
                    {activeForm === "Update Book" && <UpdateBookFormComponent />}
                    {activeForm === "Search Book" && <SearchBookFormComponent />}
                    {activeForm === "Get Member" && <GetMemberFormComponent />}
                    {activeForm === "Search Member" && <SearchMemberFormComponent />}
                    {activeForm === "Add Member" && <AddMemberFormComponent />}
                    {activeForm === "Get Transaction" && <GetTransactionFormComponent />}
                    {activeForm === "Borrow" && <BorrowFormComponent />}
                    {activeForm === "Return" && <ReturnFormComponent />}
                    {activeForm === "Reserve" && <ReserveFormComponent />}
                    {activeForm === "Get Queue" && <GetQueueFormComponent />}
                    {activeForm === "Get Member List" && <GetMemberResevationsFormComponent />}
                </OverlayForm>
            )}
        </>
    );
}