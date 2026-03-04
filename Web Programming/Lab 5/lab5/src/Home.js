import Sidebar from './Sidebar.js';
import charles from './charles.png';
import feanor from './feanor.jpg';
import anonymous from './anonymous.png';

export default function Home() {

    return (
        <div className="flex">
            <main>
                <h1>Home | Hair By Horace</h1>
                <p>Hair for sale! Horace's hair! Horace's hair for sale!</p>
                <blockquote>"Please buy my hair." —Horace</blockquote>
                <p>Are you looking for a middle-aged hair vendor? Do you want to wear Horace's hair on your own scalp? Now you can!</p>
                <p>With Hair By Horace, you can purchase Horace's hair while supplies last! Hair arrives loose and may be fashioned into a wig using a small volume of glue (sold separately), or used for arts, crafts, or any other purpose you desire. After sale, Horace Does Not Ask What You Do With His Hair!&trade;</p>
                <h2>Testimonials</h2>
                <img className="icon" src={charles}></img>
                <blockquote>"Hair By Horace saved my failing relationship! It made the perfect gift to my fiancé." —Charles</blockquote>
                <img className="icon" src={feanor}></img>
                <blockquote>"With Hair By Horace, I rekindled the light of the Two Trees of Valinor!"" —F. Curufinwë</blockquote>
                <img className="icon" src={anonymous}></img>
                <blockquote>"I'm so grateful that Horace Does Not Ask What You Do With His Hair!&trade; I really do not want to talk about it!" —Anonymous</blockquote>
            </main>
            <Sidebar />
        </div>
    )
}