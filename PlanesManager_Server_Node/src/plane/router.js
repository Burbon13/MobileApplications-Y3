import Router from 'koa-router';
import planeStore from './store';

export const router = new Router();

router.get('/', async (ctx) => {
    const response = ctx.response;
    response.body = await planeStore.find({});
    response.status = 200;
});

router.get('/:id', async (ctx) => {
    const plane = await planeStore.findOne({_id: ctx.params.id});
    const response = ctx.response;
    if (plane) {
        response.body = plane;
        response.status = 200;
    } else {
        response.status = 404;
    }
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

router.post('/', async (ctx) => await createPlane(ctx.request.body, ctx.response));

router.put('/:id', async (ctx) => {
    const plane = ctx.request.body;
    console.log(plane);
    const id = ctx.params.id;
    const planeId = plane._id;
    const response = ctx.response;
    if (planeId && planeId !== id) {
        response.body = {issue: [{error: 'Param id and body _id should be the same'}]};
        response.status = 400;
        return;
    }
    if (!planeId) {
        await createPlane(plane, response);
    } else {
        const updatedCount = await planeStore.update({_id: id}, plane);
        if (updatedCount === 1) {
            response.body = plane;
            response.status = 200;
        } else {
            response.body = {issue: [{error: 'Resource no longer exists'}]};
            response.status = 405;
        }
    }
});

router.del('/:id', async (ctx) => {
    await planeStore.remove({_id: ctx.params.id});
    ctx.response.status = 204;
});
