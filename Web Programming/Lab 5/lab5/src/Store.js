import Sidebar from './Sidebar.js';
import hair from './hair.jpg';
import buynow2 from './buynow2.gif';

export default function Store() {
  var bought = false;

  function handleclick() {
    if (bought) {
      var thanks = document.getElementById('thanks');
      thanks.textContent = "You have already purchased this item.";
    }
    else {
      var thanks = document.createElement('p');
      var text = document.createTextNode("Thank you for your purchase!");
      thanks.appendChild(text);
      thanks.id = "thanks";
      var em = document.createElement('em');
      var horace = document.createElement('p');
      text = document.createTextNode("Don't worry about giving us an address or credit card—Horace will find your home and deliver his hair within 7 business days, demanding payment at the door. That's the Horace Guarantee!");
      horace.appendChild(text);
      em.appendChild(horace);
      document.getElementsByTagName('main')[0].appendChild(thanks);
      document.getElementsByTagName('main')[0].appendChild(em);
      bought = true;
    }
  }

  return (
      <div className="Store">
        <div className="flex">
          <main>
            <h1>Store | Hair By Horace</h1>
            <p>Hair for sale! Horace's hair! Horace's hair for sale!</p>
            <h2>Horace's Hair ($12.99)</h2>
            <img src={hair} title="(this is Horace's hair)" alt="A bag of hair (Horace's)."></img>
            <img onClick={handleclick} className="right" src={buynow2}></img>
          </main>
          <Sidebar />
        </div>
      </div>
  );
};