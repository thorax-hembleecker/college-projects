import logo from './logo.svg';
import './App.css';
import Home from './Home';
import Store from './Store';
import Review from './Review';
import Layout from './Layout';
import React, { useState } from "react";
import { HashRouter as Router, Route, Routes } from 'react-router-dom';

export default function App() {

  return (
    <Router>

      <div className="App">
        <div id="container">
          <Layout />

          <Routes>
            <Route key="home" exact path="/" element={<Home/>} />
            <Route key="store" exact path="/store" element={<Store/>} />
            <Route key="review" exact path="/review" element={<Review/>} />
          </Routes>
        </div>
        <footer>
          <p>Hair By Horace &copy; 20XX Horace.</p>
        </footer>
      </div>

    </Router>
  )
}