import "./index.css";
import React, {useState, useEffect, useContext} from "react";
import {BACKEND_URL} from "../../Config";
import {AuthContext} from "../../auth";
import axios from "axios";


export function Dashboard() {

    const [assistants, setAssistants] = useState(0);
    const [chatSessions, setChatSessions] = useState(0);
    const [sortByDate, setSortByDate] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedDate, setSelectedDate] = useState(null);
    const {token, userInfo} = useContext(AuthContext);

    const formatDate = (year, month, day) => {
        if (year && month && day) {
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        }
        return '';
    };

    const widgets = [
        {
            id: "my-assistants-count",
            name: "You created",
            component: () => (
                <div className="widget-my-assistants-count">
                    <h1>{assistants} assistants</h1>
                </div>
            )
        },
        {
            id: "my-users-count",
            name: "You created",
            component: () => (
                <div className="widget-my-users-count">
                    <h1>{chatSessions} chat sessions</h1>
                </div>
            ),
        },
        {
            id: "my-deployments-status",
            name: "My Deployments Status",
            component: DeploymentWidgetContent,
        },
        {
            id: "how-to-use-this-app",
            name: "How to use this app",
            component: HowToUseThisAppWidgetContent,
        },
    ]

    useEffect(() => {
        const fetchData = async () => {
            const formattedDate = selectedDate
                ? formatDate(
                    selectedDate.getFullYear(),
                    (selectedDate.getMonth() + 1).toString().padStart(2, '0'),
                    selectedDate.getDate().toString().padStart(2, '0')
                )
                : '';

            const url = `${BACKEND_URL}/api/assistants?asc_sort=${sortByDate}&searchNameQuery=${searchTerm}&searchDateQuery=${formattedDate}`
            const url2 = `${BACKEND_URL}/api/sessions/count`
            const headers = {'Authorization': `Bearer ${token}`};

            try {
                const response = await axios.get(url, {headers});
                setAssistants(response.data.length);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    console.log(`Error calling endpoint ${url}: ${error}`);
                    if (error.response) {
                        console.error(`Error: ${error.response.status}`);
                    } else {
                        console.error(error);
                    }
                } else {
                    throw error;
                }
            }

            try {
                const response = await axios.get(url2, {headers});
                setChatSessions(response.data);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    console.log(`Error calling endpoint ${url}: ${error}`);
                    if (error.response) {
                        console.error(`Error: ${error.response.status}`);
                    } else {
                        console.error(error);
                    }
                } else {
                    throw error;
                }
            }
        };
        fetchData();
    }, [token, sortByDate, searchTerm, selectedDate]);

    return <div className="Dashboard">
        {widgets.map(widget => {
            return <Widget id={widget.id} key={widget.id} name={widget.name} component={widget.component}/>
        })}
    </div>
}

function Widget({name, component}) {
    return <div className="Widget">
        <b>{name}</b>
        {component()}
    </div>
}

function HowToUseThisAppWidgetContent() {
    const [steps] = useState([
        {
            "id": "1",
            "title": "Create an assistant",
            "component": () => <></>
        },
        {
            "id": "2",
            "title": "Test it",
            "component": () => <></>
        },
        {
            "id": "3",
            "title": "Deploy it",
            "component": () => <></>
        }
    ])
    const toComponent = (step) => (i) => {
        return <div key={i} className="how-to-use-this-app-step">
            <b>{step.title}</b>
        </div>
    }
    const divider = (i) => {
        return <svg key={i} xmlns="http://www.w3.org/2000/svg" fill="currentColor"
                    className="divider" viewBox="0 0 16 16">
            <path fillRule="evenodd"
                  d="M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8"/>
        </svg>
    }
    const stepsWithDividers = steps.reduce((result, b) => {
        return result.length === 0 ? [toComponent(b)] : [...result, divider, toComponent(b)]
    }, [])
    return (
        <div className="how-to-use-this-app-content">
            {stepsWithDividers.map((item, i) => item(i))}
        </div>
    )
}

function DeploymentWidgetContent() {
    const [deployments] = useState([
        {
            "id": "1",
            "assistantName": "My Assistant 1",
            "deploymentType": "TELEGRAM",
            "status": "HEALTHY"
        },
        {
            "id": "2",
            "assistantName": "My Assistant 1",
            "deploymentType": "REST_API",
            "status": "HEALTHY"
        },
        {
            "id": "3",
            "assistantName": "My Assistant 2",
            "deploymentType": "WEB_CHAT",
            "status": "UNHEALTHY",
        },
    ])
    return <div className="widget-my-deployments-status">
        {deployments.map(deployment => {
            return <div key={deployment.id} className="deployment">
                <div className="deployment-name">{deployment.assistantName}</div>
                <div className="deployment-type">{deployment.deploymentType}</div>
                {deployment.status === "HEALTHY" && <div className="deployment-status-healthy">Healthy</div>}
                {deployment.status === "UNHEALTHY" && <div>
                    <span className="deployment-status-unhealthy">Unhealthy</span>
                    <a>Learn more</a>
                </div>}
            </div>
        })}
    </div>
}