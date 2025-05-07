CREATE OR REPLACE FUNCTION deactivateExpiredSubscriptions()
    RETURNS TABLE(id UUID) AS $$
BEGIN
    RETURN QUERY
        UPDATE subscription
            SET is_active=false
            WHERE is_active=true AND date_end < now()
            RETURNING subscription.id;
end;
$$ LANGUAGE plpgsql;