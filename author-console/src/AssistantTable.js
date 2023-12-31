import React, {useState, useEffect, useContext} from 'react';
import './AssistantTable.css';
import {useNavigate, Link} from "react-router-dom";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {format} from "date-fns";
import Button from 'react-bootstrap/Button';
import {BACKEND_URL} from "./Config";
import axios from "axios";
import {AuthContext} from "./auth";
import SettingsWindow from "./SettingsWindow";


function AssistantTable() {

    const [assistants, setAssistants] = useState([]);
    const [sortByDate, setSortByDate] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();
    const [selectedDate, setSelectedDate] = useState(null);
    const [showSettings, setShowSettings] = useState(false);
    const {token, userInfo} = useContext(AuthContext);
    const preferredUsername = JSON.parse(JSON.stringify(userInfo)).preferred_username;

    const handleSettingsClick = () => {
        setShowSettings(!showSettings);
    };

    const handleRowClick = (assistantId) => {
        navigate('/assistants/' + assistantId);
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

            const url =`${BACKEND_URL}/api/assistants?asc_sort=${sortByDate}&searchNameQuery=${searchTerm}&searchDateQuery=${formattedDate}`
            const headers = { 'Authorization': `Bearer ${token}` };

            try {
                const response = await axios.get(url, { headers });
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
    return (
        <div className="assistant-table-page">
            <div className="chat-header">
                YagaTalk
                <div className="settings-icon" onClick={handleSettingsClick}>
                    <img src="/images/settings.png" alt="Settings"/>
                </div>
            </div>

            <div className="input-container">
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Find a assistant..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="sort-dropdown">
                    <label htmlFor="sortSelect">Sort by date:</label>
                    <select
                        id="sortSelect"
                        value={sortByDate ? 'descending' : 'ascending'}
                        onChange={(e) => setSortByDate(e.target.value === 'descending')}
                    >
                        <option value="descending">Descending date</option>
                        <option value="ascending">Ascending date</option>
                    </select>
                </div>
                <div className="date-dropdown">
                    <DatePicker
                        selected={selectedDate}
                        onChange={(date) => setSelectedDate(date)}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Select a date"
                    />
                </div>

                <div className="ml-auto"> {/* Используем ml-auto для размещения кнопки справа */}
                    <Button variant="primary" as={Link} to="/assistants/add" className="add-assistant-button btn-sm">
                        Add New Assistant
                    </Button>
                </div>
            </div>


            <div className="assistant-list">
                {assistants.map(assistant => (
                    <div className="assistant-item-container" key={assistant.name}>
                        <li
                            onClick={() => handleRowClick(assistant.id)}
                            className="assistant-item"
                        >
                            <div className="assistant-info">
                                <div className="assistant-name">{assistant.name}</div>
                                <div
                                    className="assistant-date">{format(new Date(assistant.createdTime), "yyyy-MM-dd HH:mm")}</div>
                            </div>
                        </li>
                    </div>
                ))}
            </div>
            {showSettings && (
                <SettingsWindow
                    userName={preferredUsername}
                    onClose={() => setShowSettings(false)}
                />
            )}
        </div>
    );
}

export default AssistantTable;