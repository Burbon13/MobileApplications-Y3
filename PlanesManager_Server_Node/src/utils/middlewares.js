// DEVELOPMENT PURPOSE!!!
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}



export const exceptionHandler = async (ctx, next) => {
    try {
        await sleep(1000);
        return await next();
    } catch (err) {
        ctx.body = {message: err.message || 'Unexpected error.'};
        ctx.status = err.status || 500;
    }
};

export const timingLogger = async (ctx, next) => {
    const start = Date.now();
    await next();
    console.log(`${ctx.method} ${ctx.url} => ${ctx.response.status}, ${Date.now() - start}ms`);
    console.log(ctx.response.body);
};
