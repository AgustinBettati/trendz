import React, {Component} from 'react';
import { NavLink } from 'react-router-dom';
import './AppHeader.css';

export type Props = {
    authenticated: boolean,
    onLogout: () => void
}

class AppHeader extends Component<Props> {
    render() {
        return (
            <header className="app-header">
                <div className="container">
                    <div className="app-branding">
                        <NavLink to="/" className="app-title">Home</NavLink>
                    </div>
                    <div className="app-options">
                        {this.props.authenticated && (
                            <div>
                                <NavLink className="app-option" to="/profile">Profile</NavLink>
                                <a className="app-option" onClick={this.props.onLogout}>Logout</a>
                            </div>
                        )}
                    </div>
                </div>
            </header>
        )
    }
}

export default AppHeader;
