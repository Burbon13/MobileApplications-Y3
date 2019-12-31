const Datastore = require('nedb-promises');

export class PlaneStore {
    constructor(filename) {
        this.store = Datastore.create(filename);
        this.pageSize = 15;
    }

    async find(props) {
        return this.store.find(props).sort({tailNumber: 1});
    }

    async findOne(props) {
        return this.store.findOne(props);
    }

    /**
     * Queries the planes on a certain page
     *
     * @param pageNumber the index of the desired page (first page = 0)
     * @returns {Promise<*>}
     */
    async findPage(pageNumber) {
        return this.store.find({}).sort({tailNumber: 1}).skip(pageNumber * this.pageSize).limit(this.pageSize);
    }

    async insert(plane) {
        this.validatePlane();
        return this.store.insert(plane);
    };

    async update(props, plane) {
        this.validatePlane(plane);
        return this.store.update(props, plane);
    }

    async remove(props) {
        return this.store.remove(props);
    }

    async countByBrand(brand) {
        return this.store.count({brand: brand})

    }

    validatePlane(plane) {
        let tailNumber = plane.tailNumber;
        if (!tailNumber) {
            throw new Error('Missing tail_number property');
        }
        let brand = plane.brand;
        if (!brand) {
            throw new Error('Missing brand property');
        }
        let model = plane.model;
        if (!model) {
            throw new Error('Missing model property');
        }
        let fabricationYear = plane.fabricationYear;
        if (!fabricationYear) {
            throw new Error('Missing fabrication_year property');
        }
        let engine = plane.engine;
        if (!engine) {
            throw new Error('Missing engine property');
        }
        let price = plane.price;
        if (!price) {
            throw new Error('Missing price property');
        }
    }
}

export default new PlaneStore({filename: './db/planes.json', autoload: true});
