import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./Components/LogIn.tsx";
import Register from "./Components/Register.tsx";
import ProtectedRoute from "./Components/ProtectedRoute.tsx";
import Main from "./Components/Main.tsx";
import ProtectedPage from "./Components/ProtectedPage.tsx";
import Operation from "./Components/Operation.tsx";
import AddIncome from "./Components/Operations/ManageIncome.tsx";
import PayComponent from "./Components/Operations/PayComponent.tsx";
import Historial from "./Components/Historial.tsx";
import AddLoan from "./Components/AddLoan.tsx";
import PayLoan from "./Components/Operations/PayLoan.tsx";

import "bootstrap"

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route element={<ProtectedRoute />}>
                    <Route path="/protectedpage" element={<ProtectedPage/>}/>
                    <Route path="/main" element={<Main />} />
                    <Route path="operations" element={<Operation/>}/>
                    <Route path="/operations/add" element={<AddIncome/>}/>
                    <Route path="/operations/pay" element={<PayComponent/>}/>
                    <Route path="/operations/payloan" element={<PayLoan/>}/>
                    <Route path="/historial" element={<Historial/>}/>
                    <Route path="/addloan" element={<AddLoan/>}/>

                </Route>
            </Routes>
        </Router>
    );
};

export default App;
