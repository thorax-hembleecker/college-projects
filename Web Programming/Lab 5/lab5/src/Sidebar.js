import horace from './horace.jpg';

export default function Sidebar() {
    function spin() {
        var spin = document.getElementById('spin');
        if (spin.classList.contains('spin')) {
            spin.classList.remove('spin');
            setTimeout(() => { spin.classList.add('spin'); });
        }
        else {
            spin.classList.add('spin');
        }
    }

    return (
        <aside>
            <h2>Horace's Fun And Games!</h2>
            <p>Click on Horace to spin him! What fun!</p>
            <img id="spin" className="center" onClick={spin} src={horace} title="Spin me!" alt="A beautiful image of Horace."></img>
        </aside>
    );
};