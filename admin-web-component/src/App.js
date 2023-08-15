import './App.css';
import React from 'react';
import { render } from "react-dom";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from './Login';
import Menu from './Menu';
import ContextTable from "./ContextTable";
import ContextDetail from "./Context";


const rootElement = document.getElementById("root");


function App() {
    render(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="menu" element={<Menu/>}/>
                <Route path="/menu/context-table" element={<ContextTable />}/>
                <Route path="/menu/context/:contextId" element={<ContextDetail  />}/>
            </Routes>
        </BrowserRouter>,
        rootElement
    );
}
export default App;
