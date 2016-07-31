import React, { Component } from 'react';
import { Navbar, MenuItem, NavDropdown, Nav, NavItem } from 'react-bootstrap';

class Header extends Component {
    render() {
        return (
  <Navbar>
    <Navbar.Header>
      <Navbar.Brand>
        <a href="#">Welcome to RestBucks</a>
      </Navbar.Brand>
    </Navbar.Header>
    <Nav>
      <NavItem eventKey={1} href="#">Home</NavItem>
      <NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">
        <MenuItem eventKey={3.1}>Action</MenuItem>
        <MenuItem eventKey={3.2}>Another action</MenuItem>
        <MenuItem eventKey={3.3}>Something else here</MenuItem>
        <MenuItem divider />
        <MenuItem eventKey={3.3}>Separated link</MenuItem>
      </NavDropdown>
    </Nav>
  </Navbar>)
    }
}

export default Header
