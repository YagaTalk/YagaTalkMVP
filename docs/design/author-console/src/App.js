import "./App.css";
import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LeftPanel from "./components/LeftPanel";
import {navigationTabs} from "./tabs";
import {Dashboard} from "./components/Dashboard";
import {RightPanel} from "./components/RightPanel";
import {Footer} from "./components/Footer";
import {Header} from "./components/Header";


function ConsoleBody({component}) {
    return (
        <div className={"ConsoleBody"}>
            {component()}
        </div>
    )
}


function App() {
    const route = (option) => (
        <Route key={option.tab} path={"/" + option.tab}
               element={<BasePage tab={option.tab} bodyComponent={option.renderComponent}/>}/>
    )
    return (
        <BrowserRouter>
            <Routes>
                <Route key={"default"} path="/"
                       element={<BasePage tab="dashboard" bodyComponent={() => <Dashboard/>}/>}/>
                {navigationTabs.map(route)}
            </Routes>
        </BrowserRouter>
    )
}


function BasePage({tab, bodyComponent}) {
    return (
        <div className="App fullheight">
            <Header/>
            <div className="Body">
                <LeftPanel style={{borderBottom: 'none'}} tabId={tab}/>
                <ConsoleBody component={bodyComponent}/>
                <RightPanel/>
            </div>
            <Footer/>
        </div>
    );
}

export default App;
