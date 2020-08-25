import React, {Component} from 'react';
import { NavLink } from 'react-router-dom';
import './AppHeader.css';
import logo from '../../../assets/TrendzLogo.png';

export type Props = {
    authenticated: boolean,
    onLogout: () => void
}

class AppHeader extends Component<Props> {
    render() {
        return (
            <header className="app-header">
                <div className="app-branding">
                    <img src={logo} alt={''}/>
                </div>
                <div className="app-options">
                    <NavLink to="/" className="app-option">Home</NavLink>
                    <NavLink to="/register" className="app-option">Register</NavLink>
                    <NavLink to="/profile" className="app-option">Profile</NavLink>
                    <NavLink to="/login" className="app-option">Login</NavLink>
                    {this.props.authenticated && (
                        <div>
                            <NavLink className="app-option" to="/profile">Profile</NavLink>
                            <a className="app-option" onClick={this.props.onLogout}>Logout</a>
                        </div>
                    )}
                </div>
            </header>
        )
    }
}

export default AppHeader;
