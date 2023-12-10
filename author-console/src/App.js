import "./App.css";
import React, {useEffect} from "react";
import {BrowserRouter, Route, Routes, useParams} from "react-router-dom";
import LeftPanel from "./components/LeftPanel";
import {navigationTabs} from "./tabs";
import {RightPanel} from "./components/RightPanel";
import {Footer} from "./components/Footer";
import {Header} from "./components/Header";
import {AssistantsList} from "./components/AssistantsList";
import {useNavigate} from "react-router";


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
                <Route
                    key={"default"}
                    path="/"
                    element={<RedirectToDefaultPage/>}
                />
                {navigationTabs.map(route)}
                <Route
                    key={"not-found"}
                    path="*"
                    element={<NotFoundPage/>}
                />
                <Route
                    key="/assistants/:id"
                    path="/assistants/:id"
                    element={<AssistantsListWithPreselected/>}
                />
                <Route
                    key="/assistants/new"
                    path="/assistants/new"
                    element={<AssistantsListWithNew/>}
                />
            </Routes>
        </BrowserRouter>
    )
}

function NotFoundPage() {
    return <BasePage
        tab="assistants"
        bodyComponent={() => <h1>page not found</h1>}
        hideExtras={true}
    />
}

function AssistantsListWithPreselected() {
    const {id} = useParams()
    return <BasePage
        tab="assistants"
        bodyComponent={() => <AssistantsList selectedAssistantId={id}/>}
        hideExtras={true}
    />
}

function AssistantsListWithNew() {
    return <BasePage
        tab="assistants"
        bodyComponent={() => <AssistantsList createNew={true}/>}
        hideExtras={true}
    />
}

function RedirectToDefaultPage() {
    const navigate = useNavigate()
    useEffect(() => {
        navigate("/dashboard")
    })
    return <></>
}

function BasePage({tab, bodyComponent, hideExtras}) {
    return (
        <div className="App fullheight">
            <Header/>
            <div className="AppBody">
                <LeftPanel style={{borderBottom: 'none'}} tabId={tab}/>
                <ConsoleBody component={bodyComponent}/>
                {!hideExtras && <RightPanel/>}
            </div>
            <Footer/>
        </div>
    );
}

export default App;
