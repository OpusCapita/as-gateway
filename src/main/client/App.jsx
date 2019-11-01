import React from 'react';
import { Containers } from '@opuscapita/service-base-ui';
import { Route } from 'react-router';
import Home from './components/home'

const App = () => (
    <Containers.ServiceLayout serviceName='as-gateway'>
        <Route path='/' component={Home} />
    </Containers.ServiceLayout>
);

export default Home;
