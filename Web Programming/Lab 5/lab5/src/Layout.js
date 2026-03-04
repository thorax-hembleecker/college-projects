import { Outlet, Link } from "react-router-dom";

export default function Layout() {
    return (
        <>
            <header>
                <h1>hair by horace</h1>
            </header>
            <nav>
                <ul>
                    <li>
                        <Link to="/">home</Link>
                    </li>
                    <li>
                        <Link to="/store">store</Link>
                    </li>
                    <li>
                        <Link to="/review">leave a review</Link>
                    </li>
                </ul>
            </nav>
            <Outlet />
        </>
    )
};