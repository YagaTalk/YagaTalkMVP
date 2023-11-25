import React from "react";
import {AssistantsList} from "./components/AssistantsList";
import {Dashboard} from "./components/Dashboard";
import {Deployments} from "./components/Deployments";

export const navigationTabs = [
    {
        tab: "dashboard",
        name: "Dashboard",
        renderComponent: () => <Dashboard/>
    },
    {
        tab: "my-assistants",
        name: "My Assistants",
        renderComponent: () => <AssistantsList/>
    },
    {
        tab: "deployments",
        name: "Deployments",
        renderComponent: () => <Deployments/>
    }
]