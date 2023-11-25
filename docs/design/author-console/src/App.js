import "./App.css";
import React from "react";
import {BrowserRouter, Route, Routes, useParams} from "react-router-dom";
import LeftPanel from "./components/LeftPanel";
import {navigationTabs} from "./tabs";
import {Dashboard} from "./components/Dashboard";
import {RightPanel} from "./components/RightPanel";
import {Footer} from "./components/Footer";
import {Header} from "./components/Header";
import {AssistantsList} from "./components/AssistantsList";


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
                <Route key={"not-found"} path="*" element={<h1>Not found</h1>}/>
                <Route
                    key={"/assistants/:id"}
                    path="/assistants/:id"
                    element={<AssistantsListWithPreselected/>}
                />
            </Routes>
        </BrowserRouter>
    )
}

function AssistantsListWithPreselected() {
    const { id } = useParams()
    return <BasePage
        tab="assistants"
        bodyComponent={() => <AssistantsList selectedAssistantId={id}/>}
        hideExtras={true}
    />
}

function BasePage({tab, bodyComponent, hideExtras}) {
    return (
        <div className="App fullheight">
            <Header/>
            <div className="Body">
                <LeftPanel style={{borderBottom: 'none'}} tabId={tab}/>
                <ConsoleBody component={bodyComponent}/>
                {!hideExtras && <RightPanel/>}
            </div>
            <Footer/>
        </div>
    );
}

export default App;
