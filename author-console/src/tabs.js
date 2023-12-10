import React from "react";
import {AssistantsList} from "./components/AssistantsList";
import {Dashboard} from "./components/Dashboard";
import {Deployments} from "./components/Deployments";

export const navigationTabs = [
    {
        tab: "/dashboard",
        name: "Dashboard",
        renderComponent: () => <Dashboard/>
    },
    {
        tab: "/assistants",
        name: "My Assistants",
        renderComponent: () => <AssistantsList/>,
        hideExtras: true
    },
    {
        tab: "/deployments",
        name: "Deployments",
        renderComponent: () => <Deployments/>
    }
]