import Sidebar from './Sidebar.js';
import horacethank from './horacethank.png';
import React, { useState } from "react";

export default function Review() {
    const [review, setReview] = useState("");

    function postReview() {
        if (review != null) {
            var post = document.createElement('blockquote');
            let text = document.createTextNode("\"" + review + "\" —You!");
            post.appendChild(text);
            var thank = document.createElement('img');
            thank.src = horacethank;
            thank.id = "horace";
            thank.alt = "Horace Thanks You For Your Review";
            thank.title = "I Wish You Well! —Horace";
            document.getElementsByTagName('main')[0].appendChild(post);
            document.getElementsByTagName('main')[0].appendChild(thank);
            document.getElementsByTagName('form')[0].remove();
        }
    };

    return (
        <div className="flex">
            <main>
                <h1>Leave A Review | Hair By Horace</h1>
                <form>
                    <p>Enjoyed your purchase of Hair By Horace&trade;? Let Horace know here!</p>
                    <textarea name="review" type="text" rows="5" cols="45" value={review} onChange={e => setReview(e.target.value)}></textarea>
                    <br></br>
                    <input type="submit" value="Submit Review!" onClick={postReview}></input>
                </form>
            </main>
            <Sidebar />
        </div>
    );
};