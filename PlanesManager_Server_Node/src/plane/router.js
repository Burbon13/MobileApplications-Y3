import Router from 'koa-router';
import planeStore from './store';

export const router = new Router();

const airpots = [
    // Dublin
    {
        x: 53.4264513,
        y: -6.2520985
    },
    // Otopeni
    {
        x: 44.5707306,
        y: 26.0822236
    },
    // JFK
    {
        x: 40.6413111,
        y: -73.7803278
    },
    // Schipol
    {
        x: 52.3255019,
        y: 4.6872441
    },
    // Berlin Tegel
    {
        x: 52.5570019,
        y: 13.2874535
    },
    // LAX
    {
        x: 33.9415889,
        y: -118.4107187
    },
    // Heathrow
    {
        x: 51.4700223,
        y: -0.4564842
    }
];

router.get('/', async (ctx) => {
    const response = ctx.response;
    response.body = await planeStore.find({});
    response.status = 200;
});

router.get('/:tailNumber', async (ctx) => {
    const plane = await planeStore.findOne({tailNumber: ctx.params.tailNumber});
    const response = ctx.response;
    if (plane) {
        response.body = plane;
        response.status = 200;
    } else {
        response.status = 404;
        response.body = {issue: [{error: "Plane not found"}]};
    }
});

router.get('/position/:tailNumber', async (ctx) => {
    console.log('Return random airport');
    const plane = await planeStore.findOne({tailNumber: ctx.params.tailNumber});
    const response = ctx.response;
    if (!plane) {
        response.status = 404;
        response.body = {issue: [{error: "Plane not found"}]};
    } else {
        response.body = airpots[Math.floor(Math.random() * airpots.length)];
        response.status = 200;
    }
});

router.get('/page/:pageNumber', async (ctx) => {
    const response = ctx.response;
    const pageNumber = ctx.params.pageNumber;
    response.body = await planeStore.findPage(pageNumber);
    response.status = 200;
});

router.get('/brands/count/:brands', async (ctx) => {
    const response = ctx.response;
    const brandsList = ctx.params.brands.split(',');
    let responseBody = {};

    for (let index = 0; index < brandsList.length; index++) {
        const brand = brandsList[index];
        responseBody[brand] = (await planeStore.countByBrand(brand));
    }

    response.body = responseBody;
    response.status = 200;
});

const createPlane = async (plane, response) => {
    try {
        response.body = await planeStore.insert(plane);
        response.status = 201;
    } catch (err) {
        response.body = {issue: [{error: err.message}]};
        response.status = 400;
    }
};

router.post('/', async (ctx) => {
    const plane = await planeStore.findOne({tailNumber: ctx.request.body.tailNumber});
    console.log('Plane: ' + plane);
    const response = ctx.response;
    if (plane) {
        console.log('Tail number already used');
        response.body = {issue: [{error: 'Tail number ' + ctx.request.body.tailNumber + ' already exists'}]};
        response.status = 405;
    } else {
        console.log('Inserting new plane');
        await createPlane(ctx.request.body, ctx.response)
    }
});

router.put('/', async (ctx) => {
    const plane = ctx.request.body;
    console.log(plane);
    const tailNumber = plane.tailNumber;
    const response = ctx.response;

    const updatedCount = await planeStore.update({tailNumber: tailNumber}, plane);
    if (updatedCount === 1) {
        response.body = plane;
        response.status = 200;
    } else {
        response.body = {issue: [{error: 'Resource no longer exists'}]};
        response.status = 405;
    }
});

router.del('/:tailNumber', async (ctx) => {
    await planeStore.remove({tailNumber: ctx.params.tailNumber});
    ctx.response.status = 200;
    ctx.response.body = {message: 'Success'};
});

router.post('/generate/:nr', async (ctx) => await generatePlanes(ctx.params.nr, ctx.response));


/**
 * Generates new planes and deletes the old ones
 *
 * @param nrPlanes the number of new planes
 */
const generatePlanes = async (nrPlanes, response) => {
    console.log('Deleting all planes ... :(');
    await planeStore.remove({});
    console.log('Generating new planes ... :)');

    try {
        for (let i = 1; i <= nrPlanes; i++) {
            let newPlane = {
                "tailNumber": "KLM-" + i,
                "brand": "Boeing",
                "model": "747-800",
                "fabricationYear": 2002,
                "engine": "TURBOJET",
                "price": 3431000505
            };
            await planeStore.insert(newPlane);
        }
        response.body = 'Success!';
        response.status = 201;
    } catch (err) {
        response.body = {issue: [{error: err.message}]};
        response.status = 400;
    }
};
