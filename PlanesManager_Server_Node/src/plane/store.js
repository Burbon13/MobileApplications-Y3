import dataStore from 'nedb-promise';

export class PlaneStore {
    constructor({filename, autoload}) {
        this.store = dataStore({filename, autoload});
    }

    async find(props) {
        return this.store.find(props);
    }

    async findOne(props) {
        return this.store.findOne(props);
    }

    async insert(plane) {
        let tailNumber = plane.tail_number;
        if (!tailNumber) {
            throw new Error('Missing tail_number property')
        }
        let brand = plane.brand;
        if (!brand) {
            throw new Error('Missing brand property')
        }
        let model = plane.model;
        if (!model) {
            throw new Error('Missing model property')
        }
        let fabricationYear = plane.fabrication_year;
        if (!fabricationYear) {
            throw new Error('Missing fabrication_year property')
        }
        let engine = plane.engine;
        if (!engine) {
            throw new Error('Missing engine property')
        }
        let price = plane.price;
        if (!price) {
            throw new Error('Missing price property')
        }
        return this.store.insert(plane);
    };

    async update(props, plane) {
        return this.store.update(props, plane);
    }

    async remove(props) {
        return this.store.remove(props);
    }
}

export default new PlaneStore({filename: './db/planes.json', autoload: true});
