import './index.css';
import {Button, ButtonGroup, Col, Container, Form, ListGroup, Row} from "react-bootstrap";
import {format} from "date-fns";
import React, {useContext, useEffect, useState} from "react";
import {AssistantView} from "./AssistantView";
import {useNavigate} from "react-router";
import {NewAssistant} from "./NewAssistant";
import {BACKEND_URL} from "../../Config";
import axios from "axios";
import {AuthContext} from "../../auth";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';

export function AssistantsList({selectedAssistantId, createNew}) {
    const [assistants, setAssistants] = useState([]);
    const [sortByDate, setSortByDate] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [showDatePicker, setShowDatePicker] = useState(false);
    const [selectedDate, setSelectedDate] = useState(null);
    const {token} = useContext(AuthContext);


    const handleShowDatePicker = () => {
        setShowDatePicker(true);
    };

    const handleCloseDatePicker = () => {
        setShowDatePicker(false);
    };
    const formatDate = (year, month, day) => {
        if (year && month && day) {
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        }
        return '';
    };

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
            const headers = {'Authorization': `Bearer ${token}`};

            try {
                const response = await axios.get(url, {headers});
                setAssistants(response.data);
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

    const selectedAssistant = !createNew && !!selectedAssistantId && assistants.find(assistant => assistant.id === selectedAssistantId)
    const shortAssistantsList = false

    // return (
    //     <Container fluid>
    //         <Row className="mb-3">
    //             <Col md={6}>
    //                 <Form>
    //                     <Form.Group controlId="searchTerm">
    //                         <Form.Control type="text" placeholder="Search" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
    //                     </Form.Group>
    //                 </Form>
    //             </Col>
    //             <Col md={6} className="d-flex justify-content-end">
    //                 <ButtonGroup>
    //                     <Button variant="secondary" onClick={() => setSortByDate(!sortByDate)}>
    //                         {sortByDate ? "Sort by Descending Date" : "Sort by Ascending Date"}
    //                     </Button>
    //                     <Button variant="secondary" onClick={handleShowDatePicker}>
    //                         Select Date
    //                     </Button>
    //                     {showDatePicker && (
    //                         <DatePicker
    //                             selected={selectedDate}
    //                             onChange={(date) => {
    //                                 setSelectedDate(date);
    //                                 handleCloseDatePicker();
    //                             }}
    //                             dateFormat="yyyy-MM-dd"
    //                             placeholderText="Select a date"
    //                             inline
    //                         />
    //                     )}
    //                 </ButtonGroup>
    //             </Col>
    //         </Row>
    //         <Row>
    //             <Col>
    //                 <ListGroup className="AssistantsListItems">
    //                     {assistants.map((assistant) => (
    //                         <AssistantsListItem
    //                              key={assistant.id}
    //                              assistant={assistant}
    //                              short={shortAssistantsList}
    //                              selected={assistant.id === selectedAssistant?.id} />
    //                     ))}
    //                 </ListGroup>
    //             </Col>
    //         </Row>
    //         <Row>
    //                 {createNew && <NewAssistant />}
    //                 {!!selectedAssistant && <AssistantView assistant={selectedAssistant} />}
    //         </Row>
    //     </Container>
    // );

    return <div className="AssistantsList">
        <ListGroup className="AssistantsListItems">
            {assistants.map(assistant => (
                <AssistantsListItem
                    key={assistant.id}
                    assistant={assistant}
                    short={shortAssistantsList}
                    selected={assistant.id === selectedAssistant?.id}
                />
            ))}
        </ListGroup>
        {createNew && <NewAssistant/>}
        {!!selectedAssistant && <AssistantView assistant={selectedAssistant}/>}
    </div>
}

function AssistantsListItem({assistant, short, selected}) {
    const navigate = useNavigate()
    const className = "AssistantsListItem" + (selected ? " selected" : "")
    return (
        <ListGroup.Item
            onClick={() => navigate("/assistants/" + assistant.id)}
            key={assistant.id}
            className={className}>
            <span>
                <span className="assistant-name">{assistant.name}</span>
                <AssistantStatusBadge status={assistant.status}/>
            </span>
            {!short && <>
                <p>{assistant.description}</p>
                <p>{format(new Date(assistant.createdTime), "yyyy-MM-dd HH:mm")}</p>
            </>}
        </ListGroup.Item>)
}

function AssistantStatusBadge({status}) {
    const badgesValues = {
        "active": {
            text: "active",
            className: "active"
        },
        "draft": {
            text: "draft",
            className: "draft"
        },
    }
    const badge = badgesValues[status]

    return <span className={"assistant-status-badge assistant-status-badge-" + badge.className}>{badge.text}</span>
}