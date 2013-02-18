class Bot
	attr_accessor :input
def get_input
	@input=gets
end

def is_first?
	split=@input.split(" ")
	split.length > 1 and split.first=='1'
end 

def write_out(to_write)
	print to_write 
end 

end 
