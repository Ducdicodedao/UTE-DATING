import Report from "../models/Report.js";

export const sendReport = async (req, res, next) => {
    try {
        const report = new Report(req.body);
        console.log("Send report");
        await report.save();
        res.status(200).send({
            success: true,
            message: "Send Report Success",
            result: report,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const checkReport = async (req, res, next) => {
    try {
        let report = await Report.findOne({
            receiver: req.params.userId,
            isApproved: true,
        });
        //Create empty report to check condition in android
        if (!report) {
            res.status(200).send({
                success: true,
                message: "No Exist Report",
                result: report,
                isExist: false,
            });
            return;
        }
        res.status(200).send({
            success: true,
            message: "Exist Report",
            result: report,
            isExist: true,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};
