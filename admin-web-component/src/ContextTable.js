import React, { useState, useEffect } from 'react';
import './ContextTable.css';
import {useNavigate} from "react-router-dom";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {format} from "date-fns";



function ContextTable() {

    const [contexts, setContexts] = useState([]);
    const [sortByDate, setSortByDate] = useState(true);
    const [searchTerm,setSearchTerm] = useState('');
    const navigate  = useNavigate();
    const [selectedDate, setSelectedDate] = useState(null);

    const handleRowClick = (contextId) => {
        navigate('/context/'+contextId);
    };

    const formatDate = (year, month, day) => {
        if (year && month && day) {
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        }
        return '';
    };

    useEffect(() => {
        const fetchData = () => {
            const formattedDate = selectedDate ? formatDate(selectedDate.getFullYear(), (selectedDate.getMonth() + 1).toString().padStart(2, '0'), selectedDate.getDate().toString().padStart(2, '0')) : '';

            fetch(`http://localhost:8082/api/chat/contexts?asc_sort=${sortByDate}&searchNameQuery=${searchTerm}&searchDateQuery=${formattedDate}`)
                .then(response => response.json())
                .then(data => setContexts(data))
                .catch(error => console.error('Error fetching contexts:', error));
        };

        fetchData();
    }, [sortByDate, searchTerm,selectedDate]);

    return (
        <div className="context-table-page">
            <div className="chat-header">
                YagaTalk
            </div>

            <div className="input-container">

                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Find a context..."
                        value={searchTerm}
                        onChange={e => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="sort-dropdown">
                    <label htmlFor="sortSelect">Sort by date:</label>
                    <select
                        id="sortSelect"
                        value={sortByDate ? 'descending' : 'ascending'}
                        onChange={e => setSortByDate(e.target.value === 'descending')}
                    >
                        <option value="descending">Descending date</option>
                        <option value="ascending">Ascending date</option>
                    </select>
                </div>
                <div className="date-dropdown">
                    <DatePicker
                        selected={selectedDate}
                        onChange={date => setSelectedDate(date)}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Select a date"
                    />
                </div>
            </div>

            <div className="context-list">
            {contexts.map(context => (
                <div className="context-item-container" key={context.name}>
                    <li
                        onClick={() => handleRowClick(context.id)}
                        className="context-item"
                    >
                        <div className="context-info">
                            <div className="context-name">{context.name}</div>
                            <div className="context-date">{format(new Date(context.createdTime), "yyyy-MM-dd HH:mm")}</div>
                        </div>
                    </li>
                </div>
            ))}
            </div>
        </div>
    );
}

export default ContextTable;